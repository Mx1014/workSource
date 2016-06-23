//
// EvhNewsAttachmentDTO.m
//
#import "EvhNewsAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsAttachmentDTO
//

@implementation EvhNewsAttachmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNewsAttachmentDTO* obj = [EvhNewsAttachmentDTO new];
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
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.contentUri)
        [jsonObject setObject: self.contentUri forKey: @"contentUri"];
    if(self.contentUrl)
        [jsonObject setObject: self.contentUrl forKey: @"contentUrl"];
    if(self.size)
        [jsonObject setObject: self.size forKey: @"size"];
    if(self.metadata)
        [jsonObject setObject: self.metadata forKey: @"metadata"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.contentUri = [jsonObject objectForKey: @"contentUri"];
        if(self.contentUri && [self.contentUri isEqual:[NSNull null]])
            self.contentUri = nil;

        self.contentUrl = [jsonObject objectForKey: @"contentUrl"];
        if(self.contentUrl && [self.contentUrl isEqual:[NSNull null]])
            self.contentUrl = nil;

        self.size = [jsonObject objectForKey: @"size"];
        if(self.size && [self.size isEqual:[NSNull null]])
            self.size = nil;

        self.metadata = [jsonObject objectForKey: @"metadata"];
        if(self.metadata && [self.metadata isEqual:[NSNull null]])
            self.metadata = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
