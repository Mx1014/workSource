//
// EvhOrderBriefDTO.m
//
#import "EvhOrderBriefDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrderBriefDTO
//

@implementation EvhOrderBriefDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrderBriefDTO* obj = [EvhOrderBriefDTO new];
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
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.period)
        [jsonObject setObject: self.period forKey: @"period"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.period = [jsonObject objectForKey: @"period"];
        if(self.period && [self.period isEqual:[NSNull null]])
            self.period = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
