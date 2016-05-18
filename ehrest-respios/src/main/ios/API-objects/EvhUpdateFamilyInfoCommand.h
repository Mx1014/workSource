//
// EvhUpdateFamilyInfoCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateFamilyInfoCommand
//
@interface EvhUpdateFamilyInfoCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSString* familyName;

@property(nonatomic, copy) NSString* familyDescription;

@property(nonatomic, copy) NSString* familyAvatarUri;

@property(nonatomic, copy) NSString* familyAvatarUrl;

@property(nonatomic, copy) NSString* memberNickName;

@property(nonatomic, copy) NSString* memberAvatarUri;

@property(nonatomic, copy) NSString* memberAvatarUrl;

@property(nonatomic, copy) NSString* proofResourceUri;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

