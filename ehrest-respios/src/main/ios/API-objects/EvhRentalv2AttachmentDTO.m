//
// EvhRentalv2AttachmentDTO.m
//
#import "EvhRentalv2AttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2AttachmentDTO
//

@implementation EvhRentalv2AttachmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2AttachmentDTO* obj = [EvhRentalv2AttachmentDTO new];
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
    if(self.attachmentType)
        [jsonObject setObject: self.attachmentType forKey: @"attachmentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.attachmentType = [jsonObject objectForKey: @"attachmentType"];
        if(self.attachmentType && [self.attachmentType isEqual:[NSNull null]])
            self.attachmentType = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
