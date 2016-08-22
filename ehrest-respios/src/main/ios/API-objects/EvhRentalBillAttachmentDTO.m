//
// EvhRentalBillAttachmentDTO.m
//
#import "EvhRentalBillAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalBillAttachmentDTO
//

@implementation EvhRentalBillAttachmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalBillAttachmentDTO* obj = [EvhRentalBillAttachmentDTO new];
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
    if(self.billId)
        [jsonObject setObject: self.billId forKey: @"billId"];
    if(self.attachmentType)
        [jsonObject setObject: self.attachmentType forKey: @"attachmentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

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
