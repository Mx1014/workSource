//
// EvhBuildingAttachmentDTO.m
//
#import "EvhBuildingAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingAttachmentDTO
//

@implementation EvhBuildingAttachmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBuildingAttachmentDTO* obj = [EvhBuildingAttachmentDTO new];
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
    if(self.buildingId)
        [jsonObject setObject: self.buildingId forKey: @"buildingId"];
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.contentUri)
        [jsonObject setObject: self.contentUri forKey: @"contentUri"];
    if(self.contentUrl)
        [jsonObject setObject: self.contentUrl forKey: @"contentUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.buildingId = [jsonObject objectForKey: @"buildingId"];
        if(self.buildingId && [self.buildingId isEqual:[NSNull null]])
            self.buildingId = nil;

        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.contentUri = [jsonObject objectForKey: @"contentUri"];
        if(self.contentUri && [self.contentUri isEqual:[NSNull null]])
            self.contentUri = nil;

        self.contentUrl = [jsonObject objectForKey: @"contentUrl"];
        if(self.contentUrl && [self.contentUrl isEqual:[NSNull null]])
            self.contentUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
