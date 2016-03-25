//
// EvhGetLocalizedStringCommand.h
// generated at 2016-03-25 15:57:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLocalizedStringCommand
//
@interface EvhGetLocalizedStringCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* scope;

@property(nonatomic, copy) NSString* code;

@property(nonatomic, copy) NSString* locale;

@property(nonatomic, copy) NSString* defaultValue;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

