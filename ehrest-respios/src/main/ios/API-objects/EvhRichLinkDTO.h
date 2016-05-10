//
// EvhRichLinkDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRichLinkDTO
//
@interface EvhRichLinkDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* coverUrl;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* actionUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

