//
// EvhPollDTO.m
//
#import "EvhPollDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollDTO
//

@implementation EvhPollDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPollDTO* obj = [EvhPollDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.pollId)
        [jsonObject setObject: self.pollId forKey: @"pollId"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.stopTime)
        [jsonObject setObject: self.stopTime forKey: @"stopTime"];
    if(self.pollCount)
        [jsonObject setObject: self.pollCount forKey: @"pollCount"];
    if(self.anonymousFlag)
        [jsonObject setObject: self.anonymousFlag forKey: @"anonymousFlag"];
    if(self.multiChoiceFlag)
        [jsonObject setObject: self.multiChoiceFlag forKey: @"multiChoiceFlag"];
    if(self.pollVoterStatus)
        [jsonObject setObject: self.pollVoterStatus forKey: @"pollVoterStatus"];
    if(self.processStatus)
        [jsonObject setObject: self.processStatus forKey: @"processStatus"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pollId = [jsonObject objectForKey: @"pollId"];
        if(self.pollId && [self.pollId isEqual:[NSNull null]])
            self.pollId = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.stopTime = [jsonObject objectForKey: @"stopTime"];
        if(self.stopTime && [self.stopTime isEqual:[NSNull null]])
            self.stopTime = nil;

        self.pollCount = [jsonObject objectForKey: @"pollCount"];
        if(self.pollCount && [self.pollCount isEqual:[NSNull null]])
            self.pollCount = nil;

        self.anonymousFlag = [jsonObject objectForKey: @"anonymousFlag"];
        if(self.anonymousFlag && [self.anonymousFlag isEqual:[NSNull null]])
            self.anonymousFlag = nil;

        self.multiChoiceFlag = [jsonObject objectForKey: @"multiChoiceFlag"];
        if(self.multiChoiceFlag && [self.multiChoiceFlag isEqual:[NSNull null]])
            self.multiChoiceFlag = nil;

        self.pollVoterStatus = [jsonObject objectForKey: @"pollVoterStatus"];
        if(self.pollVoterStatus && [self.pollVoterStatus isEqual:[NSNull null]])
            self.pollVoterStatus = nil;

        self.processStatus = [jsonObject objectForKey: @"processStatus"];
        if(self.processStatus && [self.processStatus isEqual:[NSNull null]])
            self.processStatus = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
