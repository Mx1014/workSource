//
// EvhRentalActionData.m
//
#import "EvhRentalActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalActionData
//

@implementation EvhRentalActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalActionData* obj = [EvhRentalActionData new];
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
    if(self.resourceTypeId)
        [jsonObject setObject: self.resourceTypeId forKey: @"resourceTypeId"];
    if(self.pageType)
        [jsonObject setObject: self.pageType forKey: @"pageType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.resourceTypeId = [jsonObject objectForKey: @"resourceTypeId"];
        if(self.resourceTypeId && [self.resourceTypeId isEqual:[NSNull null]])
            self.resourceTypeId = nil;

        self.pageType = [jsonObject objectForKey: @"pageType"];
        if(self.pageType && [self.pageType isEqual:[NSNull null]])
            self.pageType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
