//
// EvhBuildingForRentAttachmentDTO.m
//
#import "EvhBuildingForRentAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingForRentAttachmentDTO
//

@implementation EvhBuildingForRentAttachmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBuildingForRentAttachmentDTO* obj = [EvhBuildingForRentAttachmentDTO new];
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
    if(self.contentUrl)
        [jsonObject setObject: self.contentUrl forKey: @"contentUrl"];
    if(self.contentUri)
        [jsonObject setObject: self.contentUri forKey: @"contentUri"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.contentUrl = [jsonObject objectForKey: @"contentUrl"];
        if(self.contentUrl && [self.contentUrl isEqual:[NSNull null]])
            self.contentUrl = nil;

        self.contentUri = [jsonObject objectForKey: @"contentUri"];
        if(self.contentUri && [self.contentUri isEqual:[NSNull null]])
            self.contentUri = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
