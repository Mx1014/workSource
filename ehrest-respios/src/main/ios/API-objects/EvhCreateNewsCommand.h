//
// EvhCreateNewsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateNewsCommand
//
@interface EvhCreateNewsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* contentAbstract;

@property(nonatomic, copy) NSString* coverUri;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* author;

@property(nonatomic, copy) NSString* sourceDesc;

@property(nonatomic, copy) NSString* sourceUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

