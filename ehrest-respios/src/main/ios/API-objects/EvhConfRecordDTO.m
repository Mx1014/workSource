//
// EvhConfRecordDTO.m
//
#import "EvhConfRecordDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfRecordDTO
//

@implementation EvhConfRecordDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhConfRecordDTO* obj = [EvhConfRecordDTO new];
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
    if(self.confId)
        [jsonObject setObject: self.confId forKey: @"confId"];
    if(self.confDate)
        [jsonObject setObject: self.confDate forKey: @"confDate"];
    if(self.confTime)
        [jsonObject setObject: self.confTime forKey: @"confTime"];
    if(self.people)
        [jsonObject setObject: self.people forKey: @"people"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confId = [jsonObject objectForKey: @"confId"];
        if(self.confId && [self.confId isEqual:[NSNull null]])
            self.confId = nil;

        self.confDate = [jsonObject objectForKey: @"confDate"];
        if(self.confDate && [self.confDate isEqual:[NSNull null]])
            self.confDate = nil;

        self.confTime = [jsonObject objectForKey: @"confTime"];
        if(self.confTime && [self.confTime isEqual:[NSNull null]])
            self.confTime = nil;

        self.people = [jsonObject objectForKey: @"people"];
        if(self.people && [self.people isEqual:[NSNull null]])
            self.people = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
