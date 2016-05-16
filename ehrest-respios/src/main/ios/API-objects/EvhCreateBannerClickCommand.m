//
// EvhCreateBannerClickCommand.m
//
#import "EvhCreateBannerClickCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateBannerClickCommand
//

@implementation EvhCreateBannerClickCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateBannerClickCommand* obj = [EvhCreateBannerClickCommand new];
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
    if(self.bannerId)
        [jsonObject setObject: self.bannerId forKey: @"bannerId"];
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.bannerId = [jsonObject objectForKey: @"bannerId"];
        if(self.bannerId && [self.bannerId isEqual:[NSNull null]])
            self.bannerId = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
