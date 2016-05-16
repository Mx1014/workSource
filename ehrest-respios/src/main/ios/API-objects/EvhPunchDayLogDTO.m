//
// EvhPunchDayLogDTO.m
//
#import "EvhPunchDayLogDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchDayLogDTO
//

@implementation EvhPunchDayLogDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchDayLogDTO* obj = [EvhPunchDayLogDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.punchTime)
        [jsonObject setObject: self.punchTime forKey: @"punchTime"];
    if(self.workTime)
        [jsonObject setObject: self.workTime forKey: @"workTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.approvalStatus)
        [jsonObject setObject: self.approvalStatus forKey: @"approvalStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.punchTime = [jsonObject objectForKey: @"punchTime"];
        if(self.punchTime && [self.punchTime isEqual:[NSNull null]])
            self.punchTime = nil;

        self.workTime = [jsonObject objectForKey: @"workTime"];
        if(self.workTime && [self.workTime isEqual:[NSNull null]])
            self.workTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.approvalStatus = [jsonObject objectForKey: @"approvalStatus"];
        if(self.approvalStatus && [self.approvalStatus isEqual:[NSNull null]])
            self.approvalStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
