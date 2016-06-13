//
// EvhDeleteCommentAdminCommand.m
//
#import "EvhDeleteCommentAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteCommentAdminCommand
//

@implementation EvhDeleteCommentAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteCommentAdminCommand* obj = [EvhDeleteCommentAdminCommand new];
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
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.commentId)
        [jsonObject setObject: self.commentId forKey: @"commentId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.commentId = [jsonObject objectForKey: @"commentId"];
        if(self.commentId && [self.commentId isEqual:[NSNull null]])
            self.commentId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
