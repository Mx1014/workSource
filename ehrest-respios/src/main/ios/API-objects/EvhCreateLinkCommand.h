//
// EvhCreateLinkCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLinkCommand
//
@interface EvhCreateLinkCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* sourceType;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* author;

@property(nonatomic, copy) NSString* coverUri;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* contentAbstract;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

