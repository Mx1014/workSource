//
// EvhRentalSitePicDTO.m
//
#import "EvhRentalSitePicDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSitePicDTO
//

@implementation EvhRentalSitePicDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalSitePicDTO* obj = [EvhRentalSitePicDTO new];
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
    if(self.uri)
        [jsonObject setObject: self.uri forKey: @"uri"];
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.uri = [jsonObject objectForKey: @"uri"];
        if(self.uri && [self.uri isEqual:[NSNull null]])
            self.uri = nil;

        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
